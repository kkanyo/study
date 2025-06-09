import React from "react";
import Greetings from "./Greetings";

const App: React.FC = () => {
  const onClick = (name: string) => {
    console.log(`Hello, ${name}`);
  };

  return (
    <div>
      <Greetings onClick={onClick} />
    </div>
  );
};

export default App;
