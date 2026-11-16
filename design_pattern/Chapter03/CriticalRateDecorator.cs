using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter03
{
    internal class CriticalRateDecorator : AdditionalStatDecorator
    {
        public CriticalRateDecorator( EquipmentStat equipmentStat )
        {
            this.equipmentStat = equipmentStat;
        }

        public override Dictionary<string, double> GetStat()
        {
            equipmentStat.GetStat()[ "치명타 확률" ] += 0.1;

            return equipmentStat.GetStat();
        }
    }
}
