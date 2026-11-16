namespace CS_Study.Chapter04
{
    public class ChicagoStylePepperoniPizza : Pizza
    {
        public ChicagoStylePepperoniPizza()
        {
            name = "Chicago Style Deep Dish Cheese Pizza";
            dough = "Extra Thick Crust Dough";
            sauce = "Plum Tomato Sauce";

            toppings.Add( "Shredded Mozzarella Cheese" );
            toppings.Add( "Black Olives" );
            toppings.Add( "Spinach" );
            toppings.Add( "Eggplant" );
            toppings.Add( "Sliced Pepperoni" );
        }

    void Cut()
    {
        Console.WriteLine( "Cutting the pizza into square slices" );
    }
}
}
