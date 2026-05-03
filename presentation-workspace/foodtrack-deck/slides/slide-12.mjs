import { C, bg, bullet, footer, kicker, metric, rect, text, title } from "./common.mjs";

export async function slide12(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx, true);
  kicker(slide, ctx, "Cierre", true);
  title(slide, ctx, "FoodTrack queda listo para demo tecnica y mejora incremental.", true);
  metric(slide, ctx, "Hecho", "API externa integrada con datos reales", 78, 220, 250, C.green, true);
  metric(slide, ctx, "Hecho", "Boton manual + scheduler configurable", 390, 220, 270, C.blue, true);
  metric(slide, ctx, "Hecho", "Multi-admin email con manejo parcial", 720, 220, 270, C.amber, true);
  rect(slide, ctx, 88, 350, 500, 216, "#172033", { line: ctx.line("#2A3449", 1) });
  text(slide, ctx, "Siguientes pasos recomendados", 112, 374, 300, 22, { size: 16, bold: true, color: C.white });
  bullet(slide, ctx, "Configurar app password real de Gmail y key real de OpenAI.", 114, 420, 420, C.amber, true);
  bullet(slide, ctx, "Agregar Flyway/Liquibase para versionar esquema de BD.", 114, 466, 420, C.green, true);
  bullet(slide, ctx, "Guardar historico de reportes enviados y auditoria por destinatario.", 114, 512, 420, C.blue, true);
  rect(slide, ctx, 700, 350, 390, 216, "#172033", { line: ctx.line("#2A3449", 1) });
  text(slide, ctx, "Mensaje final", 724, 374, 200, 22, { size: 16, bold: true, color: C.white });
  text(slide, ctx, "El proyecto no solo vende y administra restaurante: tambien automatiza inteligencia operativa diaria desde la misma base de datos.", 724, 420, 310, 96, {
    size: 18,
    bold: true,
    title: true,
    color: "#DDE6F3",
  });
  footer(slide, ctx, 12, "FoodTrack technical presentation | backend + frontend + ReportService", true);
  return slide;
}
