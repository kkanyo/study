namespace CS_Study.Chapter04
{
    public class NewYorkStylePizzaStore : PizzaStore
    {
        public override Pizza CreatePizza( string type )
        {
            if ( type.Equals( "cheese" ) )
            {
                return new NewYorkStyleCheesePizza();
            }
            else if ( type.Equals( "veggie" ) )
            {
                return new NewYorkStyleVeggiePizza();
            }
            else if ( type.Equals( "clam" ) )
            {
                return new NewYorkStyleClamPizza();
            }
            else if ( type.Equals( "pepperoni" ) )
            {
                return new NewYorkStylePepperoniPizza();
            }
            else return null;
        }
    }
}
