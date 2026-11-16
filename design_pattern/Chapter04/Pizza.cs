using System.Text;

namespace CS_Study.Chapter04
{
    public abstract class Pizza
    {
        protected String name;
        protected String dough;
        protected String sauce;
        protected List<String> toppings = new List<String>();

        public void Prepare()
        {
            Console.WriteLine( "Prepare " + name );
            Console.WriteLine( "Tossing dough..." );
            Console.WriteLine( "Adding sauce..." );
            Console.WriteLine( "Adding toppings: " );
            foreach ( String topping in toppings )
            {
                Console.WriteLine( "   " + topping );
            }
        }

        public void Bake()
        {
            Console.WriteLine( "Bake for 25 minutes at 350" );
        }

        public virtual void Cut()
        {
            Console.WriteLine( "Cut the pizza into diagonal slices" );
        }

        public void Box()
        {
            Console.WriteLine( "Place pizza in official PizzaStore box" );
        }

        public String GetName()
        {
            return name;
        }

        public override String ToString()
        {
            StringBuilder display = new StringBuilder();
            display.Append( "---- " + name + " ----\n" );
            display.Append( dough + "\n" );
            display.Append( sauce + "\n" );
            foreach ( String topping in toppings )
            {
                display.Append( topping + "\n" );
            }
            return display.ToString();
        }
    }
}
