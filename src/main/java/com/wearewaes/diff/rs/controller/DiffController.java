package com.wearewaes.diff.rs.controller;

import com.wearewaes.diff.rs.exception.DiffException;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResponse;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.service.DiffService;
import com.wearewaes.diff.rs.transform.DiffDirection;
import com.wearewaes.diff.rs.validation.ValidUUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/v1/")
public class DiffController {

  private final DiffService diffService;

  @RequestMapping(
      value = "/diff/{diffid}/right",
      method = {RequestMethod.POST, RequestMethod.PUT},
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<DiffResponse<Void>> setDiffRight(
      final @ValidUUID @PathVariable("diffid") String diffId,
      final @Valid @RequestBody DiffRequest request) {
    return setDiffDirection(diffId, request, DiffDirection.RIGHT);
  }

  @RequestMapping(
      value = "/diff/{diffid}/left",
      method = {RequestMethod.POST, RequestMethod.PUT},
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<DiffResponse<Void>> setDiffLeft(
      final @ValidUUID @PathVariable("diffid") String diffId,
      final @Valid @RequestBody DiffRequest request) {
    return setDiffDirection(diffId, request, DiffDirection.LEFT);
  }

  private ResponseEntity<DiffResponse<Void>> setDiffDirection(
      final String diffId,
      final DiffRequest request,
      final DiffDirection direction) {
    final boolean createdOrUpdated = diffService.createOrUpdateDiff(diffId, request, direction);
    final DiffResponse<Void> diffResponse = DiffResponse.<Void>builder()
        .success(createdOrUpdated)
        .build();
    return ResponseEntity.ok(diffResponse);
  }

  @RequestMapping(
      value = "/diff/{diffid}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<DiffResponse<DiffResult>> getDiffResult(
      final @ValidUUID @PathVariable("diffid") String diffId) {
    final DiffResult diffResult = diffService.getDiffResult(diffId)
        .orElseThrow(() -> new DiffException(String.format("The ID [%s] cannot be found", diffId)));
    final DiffResponse<DiffResult> diffResponse = DiffResponse.<DiffResult>builder()
        .success(true)
        .payload(diffResult)
        .build();
    return ResponseEntity.ok(diffResponse);
  }
}