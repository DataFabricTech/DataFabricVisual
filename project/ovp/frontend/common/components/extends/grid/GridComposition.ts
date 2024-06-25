import { GridProps } from "@/components/extends/grid/GridProps";

// 말 그대로 interface 임
export interface GridComposition extends GridProps {
  onClickEvt(value: string | number): void;
}

// interface 명 기반 event 여기서 구현
// GridComposition( 여기가 paramerter 정의. vue 파일에서 넘어옴 )

// 리턴값 여기서 정의임.
// interface 에서 정의한 함수나 변수들을, 여기서 구현 한 담에 .vue 파일에서 가져다 쓸거는 return 에 넣어서 보냄.
// GridComposition {}
export function GridComposition(props: GridProps): GridComposition {
  const onClickEvt: (value: string | number) => void = () => {
    console.log("composition");
  };

  return {
    ...props,
    onClickEvt
  };
}
