namespace CS_Study.Chapter03
{
    public class DarkRoast : Beverage
    {
        public DarkRoast()
        {
            description = "Dark roast coffee";
        }

        public override double Cost()
        {
            double cost = 0.99;
            switch ( this.size )
            {
                case Size.Tall:
                    break;
                case Size.Grande:
                    cost += 0.25;
                    break;
                case Size.Venti:
                    cost += 0.5;
                    break;
            }
            return cost;
        }
    }
}
