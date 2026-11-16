namespace CS_Study.Chapter03
{
    public abstract class CondimentDecorator : Beverage
    {
        // 데코레이터가 감쌀 객체를 나타내는 형식 객체를 지정
        // 어떤 객체를 감쌀 수 있도록 기반 클래스 유형 사용
        protected Beverage beverage;

        // 모든 체코레이터에 메소드를 새로 구현하도록 추상 메소드로 선언
        public override abstract string GetDescription();
    }
}
