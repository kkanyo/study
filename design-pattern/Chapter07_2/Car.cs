// 최조 지식 원칙 예제

namespace CS_Study.Chapter07_2
{
    internal class Engine { public void Start() { }  }

    internal class Key { public bool turns() { return true;  } }

    internal class Doors { public void Lock() { } }

    internal class Car
    {
        // 구성 요소이므로 메소드 호출 가능
        Engine engine;

        public Car()
        {
            // 엔진 초기화 등을 처리
        }

        // 매개변수로 전달된 객체이므로 메소드 호출 가능
        public void Start(Key key)
        {
            // 새로운 객체를 생성하므로 메소드 호출 가능
            Doors doors = new Doors();
            bool authorized = key.turns();
            if (authorized)
            {
                engine.Start();
                //객체 내에 있는 메소드는 호출 가능
                UpdateDashboardDisplay();
                doors.Lock() ;
            }
        }

        public void UpdateDashboardDisplay()
        {
            // 디스플레이 갱신
        }
    }
}
