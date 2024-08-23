export interface IService {
  id: string;
  label: string;
  img: string;
  imgUrl: string;
  isDisabled?: boolean; // isDisabled는 일부 객체에서만 정의되었으므로 선택적 속성으로 처리
}
export interface IServiceObj {
  serviceId: string;
  defaultInfo: {
    serviceNm: string;
    serviceDesc: string;
  };
  detailInfo: Record<string, any>;

  [key: string]: any; // 문자열 인덱스 서명 추가
}

export interface ModalServiceProps {
  formData?: any;

  modalId?: string;
  style?: any;
}
