using System.Collections.Generic;
using lab14.Domain;

namespace lab14.Repository;

public interface IRepository<TId,TE> where TE : Entity<TId>
{
    IEnumerable<TE> FindAll();
}