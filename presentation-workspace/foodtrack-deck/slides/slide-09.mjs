import { C, bg, code, footer, kicker, rect, text, title } from "./common.mjs";

export async function slide09(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx, true);
  kicker(slide, ctx, "Logs y resiliencia", true);
  title(slide, ctx, "Los fallos externos quedan visibles y no tumban el flujo completo.", true);
  code(slide, ctx, `INFO  OpenAI configuration detected. Using model gpt-4o-mini
INFO  SMTP Gmail configuration detected for jacobmunoz992@gmail.com
INFO  Sales data ready: orders=2, totalSales=199000.00, topProduct=Lomo de Res
ERROR Error generating AI report. Falling back to basic report: 401 invalid_api_key
ERROR Error enviando email: 535 Username and Password not accepted
INFO  Daily report flow finished. emailSent=false`, 72, 206, 740, 300, true);
  rect(slide, ctx, 870, 214, 270, 76, "#24304A", { line: ctx.line("#3B4A66", 1) });
  text(slide, ctx, "Fallback OpenAI", 892, 234, 190, 18, { size: 14, bold: true, color: C.white });
  text(slide, ctx, "Si la key falla, se genera reporte basico con datos reales.", 892, 254, 220, 30, { size: 10, color: "#DDE6F3" });
  rect(slide, ctx, 870, 314, 270, 84, "#3A2D18", { line: ctx.line("#6B4A18", 1) });
  text(slide, ctx, "SMTP diagnostico", 892, 330, 190, 18, { size: 14, bold: true, color: C.white });
  text(slide, ctx, "El error 535 indica credencial Gmail invalida o falta app password.", 892, 352, 220, 36, { size: 10, color: "#F9E7C8" });
  rect(slide, ctx, 870, 430, 270, 76, "#1E342A", { line: ctx.line("#356E50", 1) });
  text(slide, ctx, "Entrega parcial", 892, 444, 190, 18, { size: 14, bold: true, color: C.white });
  text(slide, ctx, "El resultado enumera destinatarios enviados y fallidos.", 892, 468, 220, 30, { size: 10, color: "#D5F0E1" });
  footer(slide, ctx, 9, "Logs reales resumidos del arranque y POST /api/reports/generate-and-send", true);
  return slide;
}
