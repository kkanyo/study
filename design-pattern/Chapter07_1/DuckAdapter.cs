namespace CS_Study.Chapter07_1
{
    internal class DuckAdapter : Turkey
    {
        Duck duck;
        Random rand;

        public DuckAdapter( Duck duck )
        {
            this.duck = duck;
            rand = new Random();
        }

        public void Gobble()
        {
            duck.Quack();
        }

        public void Fly()
        {
            if ( rand.Next( 5 ) == 0 )
                duck.Fly();
        }
    }
}
