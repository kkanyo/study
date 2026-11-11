namespace CS_Study.Chapter03
{
    public abstract class Beverage
    {
        public enum Size
        {
            Tall,
            Grande,
            Venti
        };
        protected Size size = Size.Tall;

        protected string description = "No name";

        // CondimentDecorator의 GetDescription 메소드를 호출하기 위해 가상 함수로 선언
        // 동적 바인딩 참고
        public virtual string GetDescription()
        {
            return description;
        }

        public void SetSize(Size size)
        {
            this.size = size;
        }

        public Size GetSize()
        {
            return this.size;
        }

        public abstract double Cost();
    }
}
