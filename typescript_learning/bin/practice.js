"use strict";
let count = 0;
count += 1;
// count = "Hello, world!"; // Compile error: Type 'string' is not assignable to type 'number'.
const message = "Hello, world!";
const done = true;
const numbers = [1, 2, 3];
const messages = ["Hello", "World"];
// message.push(1); // Compile error: Property 'push' does not exist on type 'string'.
let mightBeUndefined = undefined; // string, undefined 두 가지 가능성
let nullableNumber = null; // number, null 두 가지 가능성
let color = "red"; // red, orange, yellow 중 하나의 값을 가져야 함
color = "yellow";
// color = "green"; // Compile error: Type '"green"' is not assignable to type '"red" | "orange" | "yellow"'.
function Sum(x, y) {
    return x + y;
}
Sum(1, 2);
