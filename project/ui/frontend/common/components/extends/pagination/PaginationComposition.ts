import {ref, computed} from "vue";
import type {PaginationProps} from "./PaginationProps";

interface PaginationComposition extends PaginationProps {
    currentPage: Ref<number>;
    paginationTotalCount: Ref<number>;
    paginationList: Ref<number[]>;
    calculatedPaginationList: Ref<number[]>;

    movePage(value: number): void;

    onChange(value: number): void;
}

export function PaginationComposition(
    props: PaginationProps,
    onchange: (page: number) => void
): PaginationComposition {

    // DATA
    const currentPage: Ref<number> = ref<number>(props.currentPageNumber);

    // COMPUTED
    const paginationTotalCount: Ref<number> = computed(() => {
        return Math.ceil(props.totalCount / props.perPage)
    })

    const paginationList: Ref<number[]> = computed(() => {
        return Array.from({length: paginationTotalCount.value}, (_, i) => i + 1);
    })

    const calculatedPaginationList: Ref<number[]> = computed(() => {
        const currentCnt: number = currentPage.value;
        const totalCnt: number = paginationTotalCount.value;

        if (currentCnt < 5) {
            return paginationList.value.slice(2, 5);
        } else if (currentCnt >= 5 && currentCnt < totalCnt - 3) {
            return paginationList.value.slice(currentCnt - 2, currentCnt + 1);
        } else {
            return paginationList.value.slice(totalCnt - 5, totalCnt - 2);
        }
    });

    // METHODS
    const onChange: (page: number) => void = (page) => {
        onchange(page);
    }

    const movePage: (page: number) => void = (page) => {
        currentPage.value = page;
        onChange(page);
    }

    return {...props, currentPage, paginationTotalCount, paginationList, calculatedPaginationList, onChange, movePage}

}