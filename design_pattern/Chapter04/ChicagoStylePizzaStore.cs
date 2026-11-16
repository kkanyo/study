namespace CS_Study.Chapter04
{
    public class ChicagoStylePizzaStore : PizzaStore
    {
        public override Pizza CreatePizza( string type )
        {
            if ( type.Equals( "cheese" ) )
            {
                return new ChicagoStyleCheesePizza();
            }
            else if ( type.Equals( "veggie" ) )
            {
                return new ChicagoStyleVeggiePizza();
            }
            else if ( type.Equals( "clam" ) )
            {
                return new ChicagoStyleClamPizza();
            }
            else if ( type.Equals( "pepperoni" ) )
            {
                return new ChicagoStylePepperoniPizza();
            }
            else return null;
        }
    }
}
