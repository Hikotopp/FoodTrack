import { C, bg, footer, kicker, rect, text, title } from "./common.mjs";

export async function slide08(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx, true);
  kicker(slide, ctx, "SOLID", true);
  title(slide, ctx, "SOLID se ve en responsabilidades pequenas y dependencias hacia contratos.", true);
  const rows = [
    ["S", "Single Responsibility", "ReportController expone endpoints; ReportProcessingService orquesta; EmailService solo envia correo."],
    ["O", "Open/Closed", "Nuevos flujos de reporte se agregan como servicios/endpoints sin tocar el dominio de ordenes."],
    ["L", "Liskov Substitution", "Puertos de repositorio permiten cambiar adaptadores sin romper los casos de uso."],
    ["I", "Interface Segregation", "UserRepositoryPort y casos de uso exponen operaciones especificas, no interfaces gigantes."],
    ["D", "Dependency Inversion", "Aplicacion depende de puertos; infraestructura implementa JPA/HTTP/SMTP."],
  ];
  rows.forEach((r, i) => {
    const y = 188 + i * 78;
    rect(slide, ctx, 82, y, 84, 52, i % 2 ? C.blue : C.green, { line: ctx.line("#FFFFFF22", 1) });
    text(slide, ctx, r[0], 105, y + 6, 40, 36, { size: 28, bold: true, title: true, color: C.white, align: "center" });
    text(slide, ctx, r[1], 196, y + 4, 250, 24, { size: 15, bold: true, color: C.white });
    text(slide, ctx, r[2], 196, y + 31, 850, 28, { size: 11, color: "#DDE6F3" });
    rect(slide, ctx, 1102, y + 13, 64, 22, i === 0 || i === 4 ? C.amber : "#2A3449", { line: ctx.line("#FFFFFF22", 1) });
    text(slide, ctx, i === 0 || i === 4 ? "clave" : "ok", 1114, y + 18, 40, 10, { size: 8, bold: true, color: C.white, align: "center" });
  });
  footer(slide, ctx, 8, "Mapeo a clases: controllers, services, ports, repositories/adapters", true);
  return slide;
}
