namespace CS_Study.Chapter03
{
    public class Espresso : Beverage
    { 
        public Espresso()
        {
            description = "Espresso";
        }

        // 데코레이터에서의 가격을 걱정할 필요 없음
        public override double Cost()
        {
            double cost = 1.99;
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
