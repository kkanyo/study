namespace CS_Study.Chapter03
{
    public class Mocha : CondimentDecorator
    {
        // 인스턴스 변수를 래핑하고자 하는 객체로 설정
        public Mocha(Beverage beverage)
        {
            this.beverage = beverage;
            this.size = beverage.GetSize();
        }

        // 장식하고 있는 객체에 작업을 위임한 다음 추가 작업
        public override string GetDescription()
        {
            return beverage.GetDescription() + ", Mocha";
        }

        public override double Cost()
        {
            double cost = 0.2;
            switch ( beverage.GetSize() )
            {
                case Size.Tall:
                    break;
                case Size.Grande:
                    cost += 0.05;
                    break;
                case Size.Venti:
                    cost += 0.1;
                    break;
            }
            return beverage.Cost() + cost;
        }
    }
}
