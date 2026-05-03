import { C, bg, footer, kicker, rect, text, title } from "./common.mjs";

export async function slide07(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "12-Factor Apps");
  title(slide, ctx, "Los factores aparecen aplicados como practicas verificables del proyecto.");
  const rows = [
    ["Codebase", "Monorepo Spring + frontend; ReportService separado"],
    ["Dependencies", "Maven/npm declarativos; MySQL/JPA/Mail/OpenAI"],
    ["Config", "DB, Gmail, OpenAI, scheduler por env vars"],
    ["Backing services", "MySQL, OpenAI y SMTP tratados como servicios conectables"],
    ["Build / Release / Run", "GitHub Actions compila, prueba, empaqueta y construye imagenes"],
    ["Processes", "Spring Boot stateless; estado persistido en MySQL"],
    ["Port binding", "8080 backend, 8081 ReportService, 4200 frontend"],
    ["Concurrency", "Servicios independientes escalables por proceso"],
    ["Disposability", "Healthchecks, logs claros y arranque por config"],
    ["Dev/prod parity", "docker-compose y variables replican entorno"],
    ["Logs", "stdout con niveles INFO/DEBUG/ERROR"],
    ["Admin processes", "Endpoint manual generate-now para tareas admin"],
  ];
  rows.forEach((r, i) => {
    const col = i < 6 ? 0 : 1;
    const x = col ? 668 : 78;
    const y = 182 + (i % 6) * 72;
    rect(slide, ctx, x, y, 520, 52, i % 2 ? C.white : "#FBFAF7", { line: ctx.line(C.line, 1) });
    text(slide, ctx, r[0], x + 16, y + 11, 136, 18, { size: 12, bold: true, color: i % 3 === 0 ? C.green : i % 3 === 1 ? C.blue : C.amber });
    text(slide, ctx, r[1], x + 162, y + 10, 330, 30, { size: 10, color: C.ink });
  });
  footer(slide, ctx, 7, "Evidencias: application.yml/properties, docker-compose.yml, GitHub Actions, endpoints admin");
  return slide;
}
