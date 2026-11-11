using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter08
{
    internal class Tea : CaffeineBeverage
    {
        internal override void Brew()
        {
            Console.WriteLine( "Steeping the tea" );
        }

        internal override void AddCondiments()
        {
            Console.WriteLine( "Adding Lemon" );
        }

        internal override bool CustomerWantsCondiments()
        {
            Console.WriteLine( "Would you like lemon with your tea (y/n)? " );
            string answer = Console.ReadLine();

            if ( answer == null ) { return false; }

            if ( answer.ToLower().StartsWith( "y" ) )
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
