// 할리우드 원칙 (Hollywood Principle)
// 저수준 구성 요소가 시스템에 접속할 수는 있지만,
// 언제, 어떻게 해당 구성 요소를 사용할지는 고수준 구성 요소가 결정

namespace CS_Study.Chapter08
{
    // 고수준 구성 요소
    // 메소드 구현이 필요한 상황에만 서브클래스를 호출
    // 클라이언트는 Tea나 Coffee와 같은 구상 클래스가 아닌
    // CaffeinBeverage에 추상화되어 있는 부분에 의존
    // 전체 시스템의 의존성을 줄일 수 있음
    internal abstract class CaffeineBeverage
    {
        // 템플릿 메소드
        // 알고리즘의 템플릿 역할
        // 템플릿 내에서 알고리즘의 각 단계는 메소드로 표현
        // 서브클래스가 알고리즘의 각 단계를 건드리지 못하도록 함
        internal void PrepareRecipe()
        {
            BoilWater();
            Brew();
            PourInCup();
            if ( CustomerWantsCondiments() )
            {
                AddCondiments();
            }
        }

        // 서브클래스에서 구현해야 하는 메소드는
        // abstract로 선언
        internal abstract void Brew();

        internal abstract void AddCondiments();

        void BoilWater()
        {
            Console.WriteLine( "Boiling water" );
        }

        void PourInCup()
        {
            Console.WriteLine( "Pouring into cup" );
        }

        // 내용이 없는 기본 메소드 (Hook)
        // 서브클래스에서 필요할 때 오버라이드해서 사용
        internal virtual bool CustomerWantsCondiments()
        {
            return false;
        }
    }
}
