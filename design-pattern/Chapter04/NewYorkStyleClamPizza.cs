namespace CS_Study.Chapter04
{
    public class NewYorkStyleClamPizza : Pizza
    {
        public NewYorkStyleClamPizza()
        {
            name = "NY Style Sauce and Cheese Pizza";
            dough = "Thin Crust Dough";
            sauce = "Marinara Sauce";

            toppings.Add( "Grated Reggiano Cheese" );
            toppings.Add( "Fresh Clams from Long Island Sound" );

        }
    }
}
