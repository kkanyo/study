namespace CS_Study.Chapter04
{
    public class NewYorkStyleCheesePizza : Pizza
    {
        public NewYorkStyleCheesePizza()
        {
		    name = "NY Style Sauce and Cheese Pizza";
		    dough = "Thin Crust Dough";
		    sauce = "Marinara Sauce";

            toppings.Add( "Grated Reggiano Cheese" );
        }
}
}
