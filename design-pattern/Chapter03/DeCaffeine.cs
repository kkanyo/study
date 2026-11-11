namespace CS_Study.Chapter03
{
    public class DeCaffeine : Beverage
    {
        public DeCaffeine()
        {
            description = "De-caffeine coffee";
        }

        public override double Cost()
        {
            double cost = 1.05;
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
