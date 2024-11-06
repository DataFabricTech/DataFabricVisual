
export default class ObjectUtils {

  /**
   * @desc      Object 내 대상 property가 있는지 확인
   *
   * @obj       property 확인 대상 Object
   * @propNames 추출하고자 하는 property명
   * @example   obj.data.sample과 같은 형태에서 최하위에 위치한 sample property가 존재하는지 확인하려면 propNames 파라미터로 'data/sample' 전달
   */
  static hasPropertyValue = (obj, propNames) => {
    let hasValue = false;
    const keys = propNames.split("/");

    for ( let i = 0; i < keys.length; i++ ) {
      const key = keys[i];
      if ( obj && obj.hasOwnProperty(key) ) {
        obj = obj[key];
        hasValue = true;
      } else {
        hasValue = false;
        break;
      }
    }

    return hasValue;
  };

  /**
   * @desc      중첩된 형태의 Object 내 property value 추출
   *
   * @obj       value 추출 대상 Object
   * @propNames 추출하고자 하는 property명
   * @example   obj.data.sample과 같은 형태에서 최하위에 위치한 sample값을 추출하려면 propNames 파라미터로 'data/sample' 전달
   */
  static findPropertyValue = (obj, propNames) => {
    let value = obj;
    const keys = propNames.split("/");

    for ( let i = 0; i < keys.length; i++ ) {
      const key = keys[i];
      if ( value && value[key] ) {
        value = value[key];
      } else {
        value = undefined;
        break;
      }
    }

    return value;
  };

  /**
   * @desc      Object 내 property value 추출
   *
   * @obj       value 추출 대상 Object
   * @propNames 추출하고자 하는 property명
   * @result    추출 대상 값이 없거나 유효하지 않은 object일 경우 반환할 기본값. 미지정 시 empty object 반환
   * @example   obj.data.sample과 같은 형태에서 최하위에 위치한 sample값을 추출하려면 propNames 파라미터로 'data/sample' 전달
   */
  static getPropertyValue = (obj, propNames, result = {}) => {
    try {
      if ( obj && propNames ) {
        if ( propNames.indexOf("/") > -1 ) {
          const value = ObjectUtils.findPropertyValue(obj, propNames);
          if ( value ) {
            result = value;
          }
        } else {
          if ( obj.hasOwnProperty(propNames) && obj[propNames] ) {
            result = obj[propNames];
          }
        }
      }
    } catch (e) {
      console.error('error occured during get values from Object');
      console.error(e);
    }

    return result;
  }

  /**
   * @desc      숫자 천단위마다 콤마 추가
   *
   * @number    콤마 추가 대상 데이터
   */
  static numberFormatter = number => number.toLocaleString();

  /**
   * @desc      Object 내 property value 추출
   *              state 등 추출 시 vuex mutation 에러 발생하여 추출 대상값을 깊은 복사하여 반환
   *
   * @obj       value 추출 대상 Object
   * @propNames 추출하고자 하는 property명
   * @result    추출 대상 값이 없거나 유효하지 않은 object일 경우 반환할 기본값. 미지정 시 empty object 반환
   * @example   obj.data.sample과 같은 형태에서 최하위에 위치한 sample값을 추출하려면 propNames 파라미터로 'data/sample' 전달
   */
  static getDeepCopiedPropertyValue = (obj, propNames, result = {}) => {
    const value = ObjectUtils.getPropertyValue(obj, propNames, result);
    return JSON.parse(JSON.stringify(value));
  }
}
