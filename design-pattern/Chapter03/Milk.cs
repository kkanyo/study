namespace CS_Study.Chapter03
{
    public class Milk : CondimentDecorator
    {
        public Milk(Beverage beverage)
        {
            this.beverage = beverage;
            this.size = beverage.GetSize();
        }

        public override string GetDescription()
        {
            return beverage.GetDescription() + ", Milk";
        }

        public override double Cost()
        {
            double cost = 0.1;
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
