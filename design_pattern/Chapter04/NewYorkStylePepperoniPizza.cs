namespace CS_Study.Chapter04
{
    public class NewYorkStylePepperoniPizza : Pizza
    {
        public NewYorkStylePepperoniPizza()
        {
            name = "NY Style Pepperoni Pizza";
            dough = "Thin Crust Dough";
            sauce = "Marinara Sauce";

            toppings.Add( "Grated Reggiano Cheese" );
            toppings.Add( "Sliced Pepperoni" );
            toppings.Add( "Garlic" );
            toppings.Add( "Onion" );
            toppings.Add( "Mushrooms" );
            toppings.Add( "Red Pepper" );
        }
    }
}
