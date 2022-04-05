const colorMap = {
  1: "rgb(245,199,202)",
  2: "rgb(187,180,213)",
  3: "rgb(255, 123, 140)",
  4: "rgb(171,139,165)",
  5: "rgb(123, 193, 255)",
  6: "rgb(123, 255, 206)",
  7: "#ffda30",
  8: "#ffbf5b",
  9: "(253,209,76)",
};

export function mappingColor(color) {
  return colorMap[color] || colorMap[1]
}

export const colorMapValues = Object.values(colorMap)
