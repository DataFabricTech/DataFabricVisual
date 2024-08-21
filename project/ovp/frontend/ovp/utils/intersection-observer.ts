export class IntersectionObserverHandler {
  private observer: IntersectionObserver;
  private readonly scrollTriggerTarget: HTMLElement | null;
  private readonly boxItemDefaultCount: number;
  private changingInitialCount: number;
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
      console.log(this.observer);
      if (entry.isIntersecting) {
        this.changingInitialCount += this.boxItemDefaultCount;
        this.getDataCallback(this.changingInitialCount, this.loader);
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

    if (rootEl.children.length > 0) {
      rootEl.scrollTop = 0;
    }
  }

  public disconnect() {
    if (this.scrollTriggerTarget) {
      this.observer.unobserve(this.scrollTriggerTarget);
    }
  }
}
