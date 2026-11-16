using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS_Study.Chapter03
{
    internal abstract class AdditionalStatDecorator : EquipmentStat
    {
        protected EquipmentStat equipmentStat;
    }
}
