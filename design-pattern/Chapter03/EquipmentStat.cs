using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter03
{
    public abstract class EquipmentStat
    {
        protected string itemName = "No Name";
        protected int itemID = -1;
        protected Dictionary<string, double> statList = new Dictionary<string, double>();

        public string GetItemName()
        {
            return itemName;
        }

        public int GetItemID()
        {
            return itemID;
        }

        public abstract Dictionary<string, double> GetStat();

        public void Display()
        {
            Console.WriteLine("ItemName : " + itemName
                + "\nItemID : " + itemID);

            Console.WriteLine( "-----------------" );
            foreach (var stat in statList )
            {
                Console.WriteLine( stat.Key + " : " + stat.Value );
            }

            Console.WriteLine();
        }
    }
}
