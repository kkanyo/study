namespace CS_Study.Chapter04
{
    public class SimplePizzaFactory
    {
        // 유일하게 구상 Pizza 클래스를 직접 참조
        // 정적 메소드로 선언하는 경우도 있음
        public Pizza CreatePizza(String type)
        {
            Pizza pizza = null;

            if ( type.Equals( "cheese" ) )
            {
                //pizza = new CheesePizza();
            }
            else if ( type.Equals( "pepperoni" ) )
            {
                //pizza = new ChicagoStylePepperoniPizza();
            }
            else if ( type.Equals( "clam" ) )
            {
                //pizza = new ChicagoStyleClamPizza();
            }
            else if ( type.Equals( "veggie" ) )
            {
                //pizza = new VeggiePizza();
            }

            return pizza;
        }
    }
}
