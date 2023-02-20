let count = 0;
count += 1;
// count = "Hello, world!"; // Compile error: Type 'string' is not assignable to type 'number'.

const message: string = "Hello, world!";

const done: boolean = true;

const numbers: number[] = [1, 2, 3];
const messages: string[] = ["Hello", "World"];

// message.push(1); // Compile error: Property 'push' does not exist on type 'string'.

let mightBeUndefined: string | undefined = undefined; // string, undefined 두 가지 가능성
let nullableNumber: number | null = null; // number, null 두 가지 가능성

let color: "red" | "orange" | "yellow" = "red"; // red, orange, yellow 중 하나의 값을 가져야 함
color = "yellow";
// color = "green"; // Compile error: Type '"green"' is not assignable to type '"red" | "orange" | "yellow"'.

function Sum(x: number, y: number): number {
  return x + y;
  //   return null; // Compile error: Type 'null' is not assignable to type 'number'.
}

console.log(Sum(1, 2));

function SumArray(numbers: number[]): number {
  return numbers.reduce((acc, current) => acc + current, 0);
}

console.log(SumArray([1, 2, 3, 4, 5]));

function returnNothing(): void {
  console.log("I am just saying hello world");
}

returnNothing();

interface Shape {
  getArea(): number; // 반드시 구현해야 함
}

class Circle implements Shape {
  radius: number;

  // 멤버 변수는 반드시 constructor에서 초기화해주어야 함
  constructor(radius: number) {
    this.radius = radius;
  }

  getArea() {
    return this.radius * this.radius * Math.PI;
  }
}

class Rectangle implements Shape {
  // accessor를 사용하면 constructor에서 선언과 초기화를 동시 진행
  // public 변수는 class 외부에서 접근 가능
  constructor(public width: number, private height: number) {
    this.width = width;
    this.height = height;
  }

  getArea() {
    return this.width * this.height;
  }
}

const shapes: Shape[] = [new Circle(5), new Rectangle(10, 5)];
shapes.forEach((shape) => {
  console.log(shape.getArea());
});

const rectangle = new Rectangle(10, 5);
console.log(rectangle.width);

interface Person {
  name: string;
  age?: number; // 설정하지 않아도 됨
}

interface Developer extends Person {
  name: string;
  age?: number;
  skills: string[];
}

const person: Person = {
  name: "kkanyo",
  age: 27,
};

const expert: Developer = {
  name: "kkanyo",
  skills: ["typecript", "react"],
};

const people: Person[] = [person, expert];
console.log(people);

// 특정 타입에 별칭을 붙이는 용도
type Player = {
  id: number;
  name?: string;
};

// Instersection(&): 두 개 이상의 타입들을 합쳐줌
type Monster = Player & {
  skills: string[];
};

const knight: Player = {
  id: 10,
};

const slime: Monster = {
  id: 100,
  skills: ["attack", "defense"],
};

type Color = "red" | "orange" | "yellow";
const fontColor: Color = "red";

/**
 * Generics
 */

function merge<T, U>(a: T, b: U): T & U {
  return {
    ...a,
    ...b,
  };
}

function wrap<T>(param: T) {
  return {
    param,
  };
}

interface Items<T> {
  list: T[];
}

const items: Items<string> = {
  list: ["a", "b", "c"],
};

class Queue<T> {
  list: T[] = [];

  get length() {
    return this.list.length;
  }

  enqueue(item: T) {
    this.list.push(item);
  }

  dequeue() {
    return this.list.shift();
  }
}

const queue = new Queue<number>();
for (let i: number = 0; i < 5; i++) {
  queue.enqueue(i);
  console.log(queue);
}
for (let i: number = 0; i < 5; i++) {
  queue.dequeue();
  console.log(queue);
}
