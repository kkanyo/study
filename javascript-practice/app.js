console.log("hello, world")

// ==== 변수, 상수 ====
let a = 7;
const b = 3;
let myName = "kkanyo";

console.log(a + b);
console.log(a * b);
console.log(a / b); // integer가 아닌 flaot형으로 출력
console.log("hello, " + myName);

myName = "kaybetsu";    // const로 선언된 경우 값 변경 불가
                        // [Uncaught TypeError: Assignment to constant variable.]
console.log("hello, " + myName);

var myLang = "Javascript"   // 과거 버전의 변수 선언 방식, 어디서든 업데이트 가능 (비명확)

const isOn = true;
console.log(isOn);

const nullvar = null;   // 비어있음 의도적으로 표현할 때 사용 (자연적으로 발생하지 않음)
let something;          // undefined, 메모리 공간은 확보되어 있는데 데이터가 없는 경우
console.log(nullvar);
console.log(something);


// ==== Array ====
const daysOfWeek = ["Mon.", "Tue.", "Wed.", "Thu.", "Fri", "Sat."];

const nonsencse = [1, 2, "hello", false, null, true, undefined, "kkanyo"];  // 반드시 같은 데이터 타입끼리 묶지 않아도 됨 (변수 또한 마찬가지)

console.log(nonsencse);
console.log(daysOfWeek[5]);
console.log(daysOfWeek[8]);  // Out of range 시 undefined 반환

daysOfWeek.push("Sun.");
console.log(daysOfWeek[6]);


// ==== Object ====
const player = {
    name: "kkanyo",
    points: 80,
    handsome: true,
    fat: "a little bit"
};

console.log(player);
console.log(player.name);
console.log(player["points"]);

player.fat = "not really";      // const로 선언되었지만 property는 수정 가능
console.log(player.fat);

player.lastName = "kyabetsu";   // 마지막에 새로 삽입됨
console.log(player);

player.points += 15;
console.log(player.points)


// ==== Function ====
function sayHello(nameOfPerson, age) {   // parameter 타입 선언 해주지 않아도 됨
    console.log("Hello! My name is " + nameOfPerson + " and I'm " + age);
}

sayHello("kkanyo", 27);
sayHello("olatte");      // parameter 전달해주지 않으면 undefined 전달
sayHello("ilyo");
sayHello("icejally", 29);
sayHello(3.3);           // 자동으로 타입 결정

function plus(a, b) {
    console.log(a + b);
}

plus(1) // 정수 연산이 불가능한 경우 NaN(Not a Number) 반환

const character = {
    name: "kkanyo",
    attack: function(enemy) {
        console.log("Attack!", enemy);
    }
};

character.attack("monster");


// ==== Recap ====