using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter08
{
    internal class Coffee : CaffeineBeverage
    {
        internal override void Brew()
        {
            Console.WriteLine( "Dripping Coffee through filter" );
        }

        internal override void AddCondiments()
        {
            Console.WriteLine( "Adding Sugar and Milk" );
        }

        internal override bool CustomerWantsCondiments()
        {
            Console.WriteLine( "Would you like milk and sugar with your coffee (y/n)? " );
            string answer = Console.ReadLine();
                
            if (answer == null) { return false; }
            
            if (answer.ToLower().StartsWith("y"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
