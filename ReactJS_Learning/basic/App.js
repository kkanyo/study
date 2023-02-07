import Button from "./Button";
import styles from "./App.module.css";
import { useState, useEffect } from "react";

function Hello() {
  // //Clean-up function: 컴포넌트가 파괴될 때 실행할 함수
  // function destroyedFunc() {
  //   console.log("bye :l");
  // }

  // function CreateFunc() {
  //   console.log("created :)");
  //   // 컴포넌트가 파괴될 때 반환한 함수 실행
  //   // TODO: 내부에서 어떻게 동작하는지 확인
  //   return destroyedFunc;
  // }

  // useEffect(CreateFunc, []);

  useEffect(() => {
    console.log("hi :)");
    return () => {
      console.log("bye :l");
    };
  }, []);
  return <h1>Hello</h1>;
}

function App() {
  const [counter, setValue] = useState(0);
  const [keyword, setKeyword] = useState("");
  const [showing, setShowing] = useState(false);
  const onClick = () => setValue((prev) => prev + 1);
  const onChange = (event) => setKeyword(event.target.value);
  const onClick2 = () => setShowing((prev) => !prev);

  // state를 변경할 때 즉, 매번 render할 때마다
  // 모든 코드를 실행하는 것은 비효율적
  console.log("I run all the time");

  // render와 상관없이 단 한 번만 실행함
  useEffect(() => {
    console.log("I run only once");
  }, []);

  // raect가 deps(복수개 가능)를 계속 관찰하며
  // deps 값에 변화가 생겼을 때만 실행함
  useEffect(() => {
    if (keyword !== "" && keyword.length > 5) {
      console.log("Search for", keyword);
    }
  }, [keyword]);

  return (
    <div>
      <input
        value={keyword}
        onChange={onChange}
        type="text"
        placeholder="Search here..."
      />
      <h1 className={styles.title}>{counter}</h1>
      <Button text={"click me"} func={onClick} />
      {/* {/*showing === false면 Component를 완전히 destroy} */}
      {showing ? <Hello /> : null}
      <button onClick={onClick2}>{showing ? "Hide" : "Show"}</button>
    </div>
  );
}

export default App;
