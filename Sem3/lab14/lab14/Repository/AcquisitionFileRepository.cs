using lab14.Domain;
using lab14.Domain.DTO;

namespace lab14.Repository;

public class AcquisitionFileRepository : InFileRepository<string, Acquisition>
{
    public AcquisitionFileRepository(string filename) : base(filename){}

    protected override Acquisition EntityFromString(string? data)
    {
        string[] attributes = data.Split(',');

        string id = attributes[0];
        string product = attributes[1];
        int amount = int.Parse(attributes[2]);
        double productPrice = double.Parse(attributes[3]);
        string idDocument = attributes[4];

        Acquisition acquisition = new Acquisition(id, product, amount, productPrice, idDocument, null);
        return acquisition;
    }
}