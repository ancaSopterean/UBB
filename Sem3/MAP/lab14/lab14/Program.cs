using System;
using lab14.Repository;

namespace lab14
{
    internal class Program
    {
        static void PrintMenu()
        {
            Console.WriteLine("Menu: ");
            Console.WriteLine("1.Sa se afiseze toate documentele (nume, dataEmitere) emise in anul 2023");
            Console.WriteLine("2.Sa se afișeze toate facturile (nume, dataScadenta) scadente in luna curenta.");
            Console.WriteLine("3.Sa se afiseze toate facturile (nume, nrProduse) cu cel putin 3 produse achizitionate.");
            Console.WriteLine("4.Sa se afișeze toate achizitiile (produs, numeFactura) din categoria Utilities.");
            Console.WriteLine("5.Sa se afișeze categoria de facturi care a înregistrat cele mai multe cheltuieli.");
            Console.WriteLine("0.Exit");
        }
        public static void Main(string[] args)
        {
            DocumentsFileRepository documentsFileRepository = 
                new DocumentsFileRepository(
                    "C:\\Users\\Anca\\Documents\\GitHub\\UBB\\Sem3\\MAP\\lab14\\lab14\\Data\\documents.txt");
            BillsFileRepository billsFileRepository =
                new BillsFileRepository(
                    "C:\\Users\\Anca\\Documents\\GitHub\\UBB\\Sem3\\MAP\\lab14\\lab14\\Data\\bills.txt");
            AcquisitionFileRepository acquisitionFileRepository =
                new AcquisitionFileRepository(
                    "C:\\Users\\Anca\\Documents\\GitHub\\UBB\\Sem3\\MAP\\lab14\\lab14\\Data\\acquisitions.txt");

            Service.Service service =
                new Service.Service(documentsFileRepository, billsFileRepository, acquisitionFileRepository);

            while (true)
            {
                PrintMenu();
                string option = Console.ReadLine();
                switch (option)
                {
                    case "1":
                    {
                        service.GetDOcumentsByYear(2023).ForEach(Console.WriteLine);
                        break;
                    }
                    case "2":
                    {
                        service.GetBillsDueToday().ForEach(Console.WriteLine);
                        break;
                    }
                    case "3":
                    {
                        service.GetBillsAtLeast3Acquisitions().ForEach(Console.WriteLine);
                        break;
                    }
                    case "4":
                    {
                        service.GetAcquisitionsFromUtilities().ForEach(Console.WriteLine);
                        break;
                    }
                    case "5":
                    {
                        Console.WriteLine(service.GetCategoryWithMostSpendings());
                        break;
                    }
                    case "0":
                    {
                        return;
                    }
                    default:
                    {
                        Console.WriteLine("Invalid option!");
                        break;
                    }
                }
            } 
        }
    }
}