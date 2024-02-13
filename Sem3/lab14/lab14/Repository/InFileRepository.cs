using System.IO;
using System.Runtime.Remoting;
using lab14.Domain;

namespace lab14.Repository;

public abstract class InFileRepository<TId,TE> : InMemoryRepository<TId,TE> where TE: Entity<TId>
{
    private string _filename;

    public InFileRepository(string filename)
    {
        _filename = filename;
        ReadFromFIle();
    }

    protected abstract TE EntityFromString(string? data);

    protected void ReadFromFIle()
    {
        if (_filename == null)
        {
            throw new RemotingException("invalid parameter");
        }

        StreamReader streamReader = new StreamReader(_filename);
        string? data;
        while (true)
        {
            data = streamReader.ReadLine();
            if (data == null)
                break;
            Save(EntityFromString(data));
        }
    }
}