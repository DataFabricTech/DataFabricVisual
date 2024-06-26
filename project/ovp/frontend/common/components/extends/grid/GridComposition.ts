import { GridProps } from "@/components/extends/grid/GridProps";
import buttonRenderer from "./grid-units/ButtonRenderer.vue";
import { ComputedRef } from "vue";

export interface GridComposition extends GridProps {
  gridApi: any;
  getDefs(): any[];
  onGridReady(params: object): void;
}

export function GridComposition(props: GridProps, BTN_FIELD_CONST: string): GridComposition {
  const gridApi = ref(null);
  const onGridReady: (params: { api: any }) => void = (params: { api: any }) => {
    gridApi.value = params.api;

    params.api.forEachNode((node: any) => {
      if (props.selectedNodes.includes(node.data[props.rowId])) {
        node.setSelected(true);
      }
    });

    // params.api.sizeColumnsToFit();
  };

  const columnWidthList: ComputedRef<any> = computed(() => {
    const widthList: any = props.columnWidthList ?? [];
    return widthList.length < 1
      ? Array(props.columnDefs.length + Object.keys(props.buttons).length).fill(100)
      : props.columnWidthList;
  });

  const getDefs: () => any[] = () => {
    // step1. button 설정을 columnDefsObject 형식으로 변환
    props.buttons.forEach((btnEl) => {
      const btnObj = {
        headerName: btnEl.headerText,
        field: `${BTN_FIELD_CONST}${btnEl.rendererKey}`,
        cellRenderer: buttonRenderer
      };

      if (btnEl.type === "button" || btnEl.type === "icon") {
        Object.assign(btnObj, {
          cellRenderer: buttonRenderer,
          cellRendererParams: {
            rowId: props.rowId,
            type: btnEl.type,
            icon: btnEl.icon || "",
            onClickRenderer: btnEl.fn,
            onOffKeys: btnEl.useOnOffOpt ? btnEl.selectedData : [],
            rendererKey: btnEl.rendererKey
          }
        });
      }

      props.columnDefs.splice(btnEl.order - 1, 0, btnObj);
    });

    props.columnDefs.map((el: any, eI: number) => {
      if (columnWidthList.value[eI] !== "auto") {
        el.width = columnWidthList.value[eI];
      }
      return el;
    });

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
    gridApi,
    onGridReady,
    getDefs
  };
}
