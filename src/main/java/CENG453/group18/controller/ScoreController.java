package CENG453.group18.controller;

import CENG453.group18.DTO.ScoreDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.entity.Score;
import CENG453.group18.service.PlayerService;
import CENG453.group18.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired private ScoreService scoreService;


    @Operation(summary = "Saves score to table, the date is not have to be today's date" , tags = { "scores", "post" })
    @PostMapping("/adjustDate")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Score.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public Score saveByAdjustingDate(String username, int playerScore, LocalDate date)
    {
        return scoreService.saveScoreByAdjustingDate(username, playerScore, date);
    }





    @Operation(summary = "Saves score to table" , tags = { "scores", "post" })
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Score.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public Score saveScore(String username, int playerScore)
    {
        return scoreService.saveScore(username, playerScore);
    }

    @Operation(summary = "Retrieve all Scores", tags = { "scores", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Score.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no Scores", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/allTime")
    public List<ScoreDTO> findAllTimeScores()
    {
        return scoreService.getAllTimeScores();
    }

    @Operation(summary = "Retrieve scores recorded in the last month", tags = { "scores", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Score.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no Scores", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/lastMonth")
    public List<ScoreDTO> findLastMonthScores()
    {
        return scoreService.getLastMonthScores();
    }

    @Operation(summary = "Retrieve scores recorded in the last week", tags = { "scores", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Score.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no Scores", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/lastWeek")
    public List<ScoreDTO> findLastWeekScores()
    {
        return scoreService.getLastWeekScores();
    }
}
