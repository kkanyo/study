import React from "react";

type GreetingsProps = {
  name: string;
  optional?: string;
  onClick: (name: string) => void;
};

const Greetings = ({ name, optional, onClick }: GreetingsProps) => {
  const handleClick = () => {
    onClick(name);
  };

  return (
    <div>
      Hello, {name}
      {optional && <p>{optional}</p>}
      <button onClick={handleClick}></button>
    </div>
  );
};

Greetings.defaultProps = {
  name: "World",
};

export default Greetings;

/**
 * Function Components
 * @description
 * 사용을 제한하는 추세
 * https://react-typescript-cheatsheet.netlify.app/docs/basic/getting-started/function_components/
 * 결론적으로는 어느 것을 사용해도 무방해보이므로, 프로젝트에 따라 나눌 것
 * 혹은 명확한 이유와 함께 사용할 것
 */

type GreetingsFCProps = {
  name: string;
  //   children?: React.ReactNode;      // ERROR: Property 'children' does not exist on type 'IntrinsicAttributes & GreetingsPoprs'.
};

const GreetingsFC: React.FC<GreetingsFCProps> = ({ name }) => {
  return <div>Hello, {name}</div>;
};

// ERROR: Property 'name' is missing in type '{}' but required in type 'GreetingsPoprs'.
GreetingsFC.defaultProps = {
  name: "World",
};

export const FCBasic = Greetings;
