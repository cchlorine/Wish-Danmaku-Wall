import pizza from "../assets/lotties/pizza.json";
import flower from "../assets/lotties/flower.json";
import ball from "../assets/lotties/ball.json";
import picture from "../assets/lotties/picture.json";
import board from "../assets/lotties/board.json";
import watch from "../assets/lotties/watch.json";
import airplane from "../assets/lotties/airplane.json";
import barbell from "../assets/lotties/barbell.json";
import cloneDeep from "lodash.clonedeep";

const lottieAnimationMap = {
  1: pizza,
  2: flower,
  3: ball,
  4: picture,
  5: watch,
  6: airplane,
  7: barbell,
  8: board,
};

export function mappingPicture(idx) {
  return cloneDeep(lottieAnimationMap[idx] || lottieAnimationMap[1])
}
