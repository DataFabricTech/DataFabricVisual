export class IntersectionObserverHandler {
  private observer: IntersectionObserver;
  private readonly scrollTriggerTarget: HTMLElement | null;
  private readonly boxItemDefaultCount: number;
  private changingInitialCount: number;
  private readonly targetId: string;
  private readonly loader: HTMLElement | null;
  private readonly getDataCallback: (
    count: number,
    loader: HTMLElement | null,
  ) => void;

  constructor(
    targetId: string,
    scrollTriggerTarget: HTMLElement | null,
    loaderId: string,
    changingInitialCount: number,
    boxItemDefaultCount: number,
    getDataCallback: (count: number, loader: HTMLElement | null) => void,
    rootMargin?: string,
    threshold?: number,
  ) {
    this.targetId = targetId;
    this.scrollTriggerTarget = scrollTriggerTarget;
    this.changingInitialCount = changingInitialCount;
    this.boxItemDefaultCount = boxItemDefaultCount;
    this.loader = document.getElementById(loaderId);
    this.getDataCallback = getDataCallback;

    this.observer = new IntersectionObserver(this.handleIntersect.bind(this), {
      root: document.getElementById(targetId),
      rootMargin: rootMargin ?? "10px",
      threshold: threshold ?? 0.5,
    });

    if (this.scrollTriggerTarget) {
      this.observer.observe(this.scrollTriggerTarget);
    }
  }

  private handleIntersect(entries: IntersectionObserverEntry[]) {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const rootEl: any = document.getElementById(this.targetId);
        const result =
          rootEl !== null &&
          (rootEl.scrollTop > 0 || rootEl.scrollHeight > rootEl.clientHeight);

        if (result) {
          this.changingInitialCount += this.boxItemDefaultCount;
          this.getDataCallback(this.changingInitialCount, this.loader);
        }
      }
    });
  }

  /**
   * 목록이 갱신되었을때 netChangingInitialCount 를 reset 해주는 fn.
   * @param newChangingInitialCount
   */
  public updateChangingInitialCount(newChangingInitialCount: number) {
    this.changingInitialCount = newChangingInitialCount;
  }

  /**
   * 목록이 갱신되었을때 scroll 을 첫번째 항목으로 보내는 fn.
   */
  public scrollToFirElement() {
    const rootEl: any = this.observer.root;
    if (rootEl === null) {
      return;
    }

    if (rootEl.children.length > 0) {
      // NOTE : modal 사용 방식이 변경되면서 가짜 modal 이 dom 에 생성됨. -> rootEl 이 제대로 된 element 를 못잡아서 아래 코드처럼 구현함.
      const targetId = rootEl.id;
      document.querySelectorAll(`#${targetId}`).forEach((el) => {
        el.scrollTop = 0;
      });
    }
  }

  public disconnect() {
    if (this.scrollTriggerTarget) {
      this.observer.unobserve(this.scrollTriggerTarget);
    }
  }
}
