import { CytoscapeNodeStyle, CytoscapeEdgeStyle } from "./lineage";

const nodeStyles: CytoscapeNodeStyle[] = [
  {
    selector: "node",
    style: {
      "min-width": "100px",
      width: "100px",
      height: "25px",
      shape: "rectangle",
      padding: "8px 12px",
      "background-color": "#f7f9fc",
      "text-valign": "center",
      "text-halign": "center",
      color: "#31353f",
      "border-width": "0.5px",
      "border-color": "#dbe0e8",
      "border-style": "solid",
      "overlay-color": "transparent",
      "overlay-opacity": 0,
    },
  },
  {
    selector: "node:selected",
    style: {
      "border-color": "#016d77",
    },
  },
];
const edgeStyles: CytoscapeEdgeStyle[] = [
  {
    selector: "edge",
    style: {
      width: 0.5,
      "line-color": "#dbe0e8",
      "target-arrow-color": "#dbe0e8",
      "target-arrow-shape": "triangle",
      "curve-style": "unbundled-bezier",
      "source-endpoint": "50% 0%",
      "target-endpoint": "-50% 0%",
      "overlay-color": "transparent",
      "overlay-opacity": 0,
    },
  },
  {
    selector: "edge:selected",
    style: {
      "line-color": "#016d77",
      "target-arrow-color": "#016d77",
    },
  },
];
const lineageStyle = [...nodeStyles, ...edgeStyles];
export default lineageStyle;
