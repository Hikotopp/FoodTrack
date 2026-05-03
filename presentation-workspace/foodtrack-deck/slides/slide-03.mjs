import { C, bg, bullet, chip, footer, kicker, rect, text, title } from "./common.mjs";

export async function slide03(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Implementacion de modulos");
  title(slide, ctx, "El backend separa entrada, aplicacion, dominio e infraestructura.");
  const layers = [
    ["entrypoint", "Controllers REST: ordenes, usuarios, reportes admin", C.green2],
    ["application", "Casos de uso y servicios: orquestan reglas sin depender de frameworks", C.blue2],
    ["domain", "Entidades y contratos del negocio: ordenes, usuarios, mesas, productos", "#FFF0D5"],
    ["infrastructure", "JPA repositories, adaptadores externos, persistencia MySQL", "#EFE7FF"],
  ];
  layers.forEach((l, i) => {
    const y = 198 + i * 88;
    rect(slide, ctx, 70, y, 482, 62, l[2], { line: ctx.line(C.line, 1) });
    text(slide, ctx, l[0], 92, y + 12, 132, 22, { size: 16, bold: true, color: C.ink });
    text(slide, ctx, l[1], 238, y + 13, 288, 36, { size: 10.5, color: C.soft });
  });
  chip(slide, ctx, "Report endpoint", 705, 198, 170, C.blue2, C.blue);
  bullet(slide, ctx, "AdminReportController llama al ReportService por HTTP.", 704, 245, 420, C.blue);
  chip(slide, ctx, "Ordenes", 705, 314, 115, C.green2, C.green);
  bullet(slide, ctx, "RestaurantTableApplicationService persiste y registra cierres de orden.", 704, 360, 430, C.green);
  chip(slide, ctx, "Usuarios", 705, 430, 115, "#FFF0D5", C.amber);
  bullet(slide, ctx, "UserAdministrationApplicationService controla borrado, rol admin y reasignaciones.", 704, 476, 430, C.amber);
  text(slide, ctx, "La estructura permite agregar ReportService sin mezclar la logica de reportes IA con el nucleo de ventas.", 705, 570, 430, 48, {
    size: 15,
    bold: true,
    title: true,
    color: C.ink,
  });
  footer(slide, ctx, 3, "Backend_FoodTrack/src/main/java/.../entrypoint, application, domain, infrastructure");
  return slide;
}
