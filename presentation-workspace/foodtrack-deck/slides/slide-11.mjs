import { C, bg, footer, kicker, rect, text, title } from "./common.mjs";

export async function slide11(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Testing e integracion");
  title(slide, ctx, "La prueba local confirmo la cadena completa; quedan credenciales reales por configurar.");
  const rows = [
    ["Backend", "compile + test", "OK"],
    ["ReportService", "compile + arranque 8081", "OK"],
    ["Frontend", "npm run build", "OK"],
    ["Integracion", "4200 -> 8080 -> 8081 -> MySQL", "OK"],
    ["OpenAI", "key placeholder genera fallback", "PENDIENTE"],
    ["Gmail", "app password invalida: error 535", "PENDIENTE"],
  ];
  rows.forEach((r, i) => {
    const y = 188 + i * 66;
    rect(slide, ctx, 88, y, 930, 46, i % 2 ? C.white : "#FBFAF7", { line: ctx.line(C.line, 1) });
    text(slide, ctx, r[0], 110, y + 12, 145, 16, { size: 12.5, bold: true, color: C.ink });
    text(slide, ctx, r[1], 280, y + 12, 470, 16, { size: 11, color: C.soft });
    const ok = r[2] === "OK";
    rect(slide, ctx, 846, y + 10, 112, 24, ok ? C.green2 : C.amber2, { line: ctx.line(ok ? C.green : C.amber, 1) });
    text(slide, ctx, r[2], 862, y + 16, 80, 10, { size: 8.5, bold: true, align: "center", color: ok ? C.green : "#6B3D07" });
  });
  text(slide, ctx, "Lectura de prueba: se detectaron 2 ordenes, $199.000 en ventas y un producto top. El envio fallo por credencial Gmail, no por codigo.", 88, 604, 940, 38, {
    size: 14,
    bold: true,
    title: true,
    color: C.ink,
  });
  footer(slide, ctx, 11, "Validacion local 2026-05-02 | requiere OPENAI_API_KEY y GMAIL_PASSWORD reales");
  return slide;
}
