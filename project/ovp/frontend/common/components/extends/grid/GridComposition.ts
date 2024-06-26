import { GridProps } from "@/components/extends/grid/GridProps";
import buttonRenderer from "./grid-units/ButtonRenderer.vue";
import valFuncRenderer from "./grid-units/ValFuncRenderer.vue";
import htmlRenderer from "./grid-units/HtmlRenderer.vue";
import _ from "lodash";

export interface GridComposition extends GridProps {
  getDefs(): any[];
  onGridReady(params: object): void;
}

export function GridComposition(props: GridProps, BTN_FIELD_CONST: string): GridComposition {
  const rowId: string | number | undefined = props.rowId || "id";
  const selectedNodes: any[] = props.selectedNodes || [];
  const buttons: any[] = props.buttons || [];
  const columnRender: object = props.columnRender || [];

  const onGridReady: (params: { api: any }) => void = (params: { api: any }) => {
    params.api.forEachNode((node: any) => {
      if (selectedNodes.includes(node.data[rowId])) {
        node.setSelected(true);
      }
    });

    if (props.setColumnFit) {
      params.api.sizeColumnsToFit();
    }
  };

  const columnWidthList: any = computed(() => {
    return props.columnWidthList?.length ? props.columnWidthList : [];
  });

  const getDefs: () => any[] = () => {
    // step1. button 설정을 columnDefsObject 형식으로 변환
    buttons.forEach((btnEl: any, bI: number) => {
      const btnObj = {
        headerName: btnEl.headerText,
        field: `${BTN_FIELD_CONST}${bI}`,
        cellRenderer: buttonRenderer
      };

      if (btnEl.rendererType === "button") {
        Object.assign(btnObj, {
          cellRenderer: buttonRenderer,
          cellRendererParams: {
            rowId: rowId,
            type: btnEl.type,
            icon: btnEl.icon || "",
            buttonText: btnEl.buttonText || "",
            onClickRenderer: btnEl.fn,
            onOffKeys: btnEl.type === "onOff" ? btnEl.selectedData : []
          }
        });
      }

      props.columnDefs.splice(btnEl.order - 1, 0, btnObj);
    });

    // props.columnDefs에 renderer 설정한게 있으면 적용한다.
    if (Object.keys(columnRender).length > 0) {
      _.forOwn(columnRender, (obj: any, key: string) => {
        const defs = _.find(props.columnDefs, { field: key });

        if (obj.type === "valFunc") {
          defs.cellRenderer = valFuncRenderer;
          defs.cellRendererParams = {
            fn: obj.fn
          };
        }
        if (obj.type === "html") {
          defs.cellRenderer = htmlRenderer;
          defs.cellRendererParams = {
            fn: obj.fn
          };
        }
      });
    }

    // width 지정한 게 있으면 반영한다.
    if (columnWidthList.value.length > 0) {
      props.columnDefs.map((el: any, eI: number) => {
        el.width = columnWidthList.value[eI];
        return el;
      });
    }

    const checkboxDefs: any[] = [];
    // step2. 첫번째 column 에 checkbox 를 추가해준다.
    if (props.useRowCheckBox) {
      checkboxDefs.push({
        width: 50,
        headerName: "",
        field: "AG_GRID_check",
        headerCheckboxSelection: true,
        checkboxSelection: true,
        showDisabledCheckboxes: true
      });
    }

    return checkboxDefs.concat(props.columnDefs);
  };

  return {
    ...props,
    onGridReady,
    getDefs
  };
}
