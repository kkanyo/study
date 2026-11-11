using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter03
{
    internal class AttackPointDecorator : AdditionalStatDecorator
    {
        public AttackPointDecorator(EquipmentStat equipmentStat)
        {
            this.equipmentStat = equipmentStat;
        }

        public override Dictionary<string, double> GetStat()
        {
            equipmentStat.GetStat()[ "공격력" ] += 100;

            return equipmentStat.GetStat();
        }
    }
}
