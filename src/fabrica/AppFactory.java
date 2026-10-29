package fabrica;

import UTPsports.UTPsports;
import repositorio.ClaseRepository;
import repositorio.ClaseRepositoryJdbc;
import repositorio.EmpleadoRepository;
import repositorio.EmpleadoRepositoryJdbc;
import repositorio.FacturaRepository;
import repositorio.FacturaRepositoryJdbc;
import repositorio.SucursalRepository;
import repositorio.SucursalRepositoryJdbc;
import repositorio.CategoriaRepository;
import repositorio.CategoriaRepositoryJdbc;
import repositorio.ProveedorRepository;
import repositorio.ProveedorRepositoryJdbc;
import repositorio.EquipoRepository;
import repositorio.EquipoRepositoryJdbc;
import repositorio.EmpresaRepository;
import repositorio.EmpresaRepositoryJdbc;
import repositorio.AutenticacionRepository;
import repositorio.AutenticacionRepositoryJdbc;
import repositorio.MembresiaClienteRepository;
import repositorio.MembresiaClienteRepositoryJdbc;
import repositorio.AsistenciaRepository;
import repositorio.AsistenciaRepositoryJdbc;
import repositorio.PagoClienteRepository;
import repositorio.PagoClienteRepositoryJdbc;
import repositorio.PlanRepository;
import repositorio.PlanRepositoryJdbc;
import repositorio.ProductoRepository;
import repositorio.ProductoRepositoryJdbc;
import repositorio.VentaRepository;
import repositorio.VentaRepositoryJdbc;
import repositorio.ClienteRepository;
import repositorio.ClienteRepositoryJdbc;
import repositorio.MembresiaRepository;
import repositorio.MembresiaRepositoryJdbc;
import repositorio.PagoRepository;
import repositorio.PagoRepositoryJdbc;
import repositorio.AvisoRepository;
import repositorio.AvisoRepositoryJdbc;
import repositorio.ReporteRepository;
import repositorio.ReporteRepositoryJdbc;
import servicio.ClaseService;
import servicio.EmpleadoService;
import servicio.FacturaService;
import servicio.SucursalService;
import servicio.CategoriaService;
import servicio.ProveedorService;
import servicio.EquipoService;
import servicio.EmpresaService;
import servicio.AutenticacionService;
import servicio.MembresiaClienteService;
import servicio.AsistenciaService;
import servicio.PagoClienteService;
import servicio.PlanService;
import servicio.ProductoService;
import servicio.VentaService;
import servicio.ClienteService;
import servicio.MembresiaService;
import servicio.PagoService;
import servicio.AvisoService;
import servicio.ReporteService;

/**
 * Punto único de ensamblado de la aplicación (patrón Singleton + Factory).
 * <p>
 * {@code AppFactory} centraliza la construcción de repositorios y servicios,
 * desacoplando a los formularios (UI) de esos detalles: una sola instancia
 * de la fábrica y de cada servicio para toda la aplicación.
 * <p>
 * <b>Importante sobre la conexión:</b> cada {@link repositorio repositorio}
 * sigue creando su propia instancia de {@link UTPsports} justo antes de
 * cada operación (exactamente como hacía el código original en cada
 * formulario), en vez de reutilizar una única instancia cacheada aquí.
 * Esto es intencional: {@link UTPsports#getSharedConnection()} valida y
 * reconecta automáticamente si la conexión compartida quedó cerrada o
 * inválida (por timeout del servidor, u otra operación que la cerró), y esa
 * revalidación solo ocurre al construir un {@code UTPsports} nuevo. Si
 * {@code AppFactory} cacheara una única instancia (como en una versión
 * anterior de este refactor), esa revalidación dejaría de ocurrir después
 * del primer uso, y cualquier corte de conexión posterior se propagaría
 * como {@code SQLNonTransientConnectionException} en toda la aplicación.
 * La lógica de conexión/licencia en sí (host, credenciales, autenticación)
 * permanece 100% intacta dentro de {@link UTPsports}; esta fábrica y los
 * repositorios solo la reutilizan a través de su API pública existente.
 */
public final class AppFactory {

    private static volatile AppFactory instancia;

    private final ClaseService claseService;
    private final EmpleadoService empleadoService;
    private final FacturaService facturaService;
    private final SucursalService sucursalService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final EquipoService equipoService;
    private final EmpresaService empresaService;
    private final AutenticacionService autenticacionService;
    private final MembresiaClienteService membresiaClienteService;
    private final AsistenciaService asistenciaService;
    private final PagoClienteService pagoClienteService;
    private final PlanService planService;
    private final ProductoService productoService;
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final AvisoService avisoService;
    private final ReporteService reporteService;

    private AppFactory() {
        ClaseRepository claseRepository = new ClaseRepositoryJdbc();
        EmpleadoRepository empleadoRepository = new EmpleadoRepositoryJdbc();
        FacturaRepository facturaRepository = new FacturaRepositoryJdbc();
        SucursalRepository sucursalRepository = new SucursalRepositoryJdbc();
        CategoriaRepository categoriaRepository = new CategoriaRepositoryJdbc();
        ProveedorRepository proveedorRepository = new ProveedorRepositoryJdbc();
        EquipoRepository equipoRepository = new EquipoRepositoryJdbc();
        EmpresaRepository empresaRepository = new EmpresaRepositoryJdbc();
        AutenticacionRepository autenticacionRepository = new AutenticacionRepositoryJdbc();
        MembresiaClienteRepository membresiaClienteRepository = new MembresiaClienteRepositoryJdbc();
        AsistenciaRepository asistenciaRepository = new AsistenciaRepositoryJdbc();
        PagoClienteRepository pagoClienteRepository = new PagoClienteRepositoryJdbc();
        PlanRepository planRepository = new PlanRepositoryJdbc();
        ProductoRepository productoRepository = new ProductoRepositoryJdbc();
        VentaRepository ventaRepository = new VentaRepositoryJdbc();
        ClienteRepository clienteRepository = new ClienteRepositoryJdbc();
        MembresiaRepository membresiaRepository = new MembresiaRepositoryJdbc();
        PagoRepository pagoRepository = new PagoRepositoryJdbc();
        AvisoRepository avisoRepository = new AvisoRepositoryJdbc();
        ReporteRepository reporteRepository = new ReporteRepositoryJdbc();
        this.claseService = new ClaseService(claseRepository);
        this.empleadoService = new EmpleadoService(empleadoRepository);
        this.facturaService = new FacturaService(facturaRepository);
        this.sucursalService = new SucursalService(sucursalRepository);
        this.categoriaService = new CategoriaService(categoriaRepository);
        this.proveedorService = new ProveedorService(proveedorRepository);
        this.equipoService = new EquipoService(equipoRepository);
        this.empresaService = new EmpresaService(empresaRepository);
        this.autenticacionService = new AutenticacionService(autenticacionRepository);
        this.membresiaClienteService = new MembresiaClienteService(membresiaClienteRepository);
        this.asistenciaService = new AsistenciaService(asistenciaRepository);
        this.pagoClienteService = new PagoClienteService(pagoClienteRepository);
        this.planService = new PlanService(planRepository);
        this.productoService = new ProductoService(productoRepository);
        this.ventaService = new VentaService(ventaRepository);
        this.clienteService = new ClienteService(clienteRepository);
        this.membresiaService = new MembresiaService(membresiaRepository);
        this.pagoService = new PagoService(pagoRepository);
        this.avisoService = new AvisoService(avisoRepository);
        this.reporteService = new ReporteService(reporteRepository);
    }

    public static AppFactory getInstance() {
        AppFactory resultado = instancia;
        if (resultado == null) {
            synchronized (AppFactory.class) {
                resultado = instancia;
                if (resultado == null) {
                    instancia = resultado = new AppFactory();
                }
            }
        }
        return resultado;
    }

    public ClaseService claseService() {
        return claseService;
    }

    public EmpleadoService empleadoService() {
        return empleadoService;
    }

    public FacturaService facturaService() {
        return facturaService;
    }

    public SucursalService sucursalService() {
        return sucursalService;
    }

    public CategoriaService categoriaService() {
        return categoriaService;
    }

    public ProveedorService proveedorService() {
        return proveedorService;
    }

    public EquipoService equipoService() {
        return equipoService;
    }

    public EmpresaService empresaService() {
        return empresaService;
    }

    public AutenticacionService autenticacionService() {
        return autenticacionService;
    }

    public MembresiaClienteService membresiaClienteService() {
        return membresiaClienteService;
    }

    public AsistenciaService asistenciaService() {
        return asistenciaService;
    }

    public PagoClienteService pagoClienteService() {
        return pagoClienteService;
    }

    public PlanService planService() {
        return planService;
    }

    public ProductoService productoService() {
        return productoService;
    }

    public VentaService ventaService() {
        return ventaService;
    }

    public ClienteService clienteService() {
        return clienteService;
    }

    public MembresiaService membresiaService() {
        return membresiaService;
    }

    public PagoService pagoService() {
        return pagoService;
    }

    public AvisoService avisoService() {
        return avisoService;
    }

    public ReporteService reporteService() {
        return reporteService;
    }
}
