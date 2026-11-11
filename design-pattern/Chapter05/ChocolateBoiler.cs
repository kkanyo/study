namespace CS_Study.Chapter05
{
    sealed class ChocolateBoiler
    {
        private ChocolateBoiler() { }

        private static readonly ChocolateBoiler mChocolateBoilerInstance = new ChocolateBoiler();

        private bool empty;
        private bool boiled;

        public void Fill()
        {
            if ( IsEmpty() )
            {
                empty = false;
                boiled = false;
            }
        }

        public void Drain()
        {
            if ( !IsEmpty() && IsBoiled() )
            {
                empty = true;
            }
        }

        public void Boil()
        {
            if ( !IsEmpty() && !IsBoiled() )
            {
                boiled = true;
            }
        }

        public bool IsEmpty()
        {
            return empty;
        }

        public bool IsBoiled()
        {
            return boiled;
        }
    }
}
