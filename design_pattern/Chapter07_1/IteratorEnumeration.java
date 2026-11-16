namespace CS_Study.Chapter07_1
{
    internal class IteratorEnumeration : Enumeration<Object>
    {
        Iterator<T> iterator;

        public IteratorEnumeration(Iterator iterator)
        {
            this.iterator = iterator;
        }

        public boolean hasMoreElements()
        {
            return iterator.hasNext();
        }

        public T nextElement()
        {
            return iterator.next();
        }
    }
}
