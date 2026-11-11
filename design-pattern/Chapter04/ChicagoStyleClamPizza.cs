namespace CS_Study.Chapter04
{
    public class ChicagoStyleClamPizza : Pizza
    {
        public ChicagoStyleClamPizza()
        {
            name = "Chicago Style Deep Dish Cheese Pizza";
            dough = "Extra Thick Crust Dough";
            sauce = "Plum Tomato Sauce";

            toppings.Add( "Shredded Mozzarella Cheese" );
            toppings.Add( "Frozen Clams from Chesapeake Bay" );
        }

        void Cut()
    {
        Console.WriteLine( "Cutting the pizza into square slices" );
    }
}
}
