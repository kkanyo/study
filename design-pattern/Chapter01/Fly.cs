namespace CS_Study.Chapter01
{
    public class FlyWithWings : IFlyBehavior
    {
        public void Fly()
        {
            Console.WriteLine( "FlyWithWings" );
        }
    }

    public class FlyNoWay : IFlyBehavior
    {
        public void Fly()
        {
            Console.WriteLine( "FlyNoWay" );
        }
    }

    public class FlyRocketPowered : IFlyBehavior
    {
        public void Fly()
        {
            Console.WriteLine( "FlyRocketPowered" );
        }
    }
}